//-----------------------------------------------------------//
//  ivisPro FS 2018
//  
//  Kevin Kirn <kevin.kirn@students.fhwn.ch>
//  Ken Iseli <ken.iseli@students.fhnw.ch>
//-----------------------------------------------------------//
var API_STATIONS_URL = 'http://46.101.218.251/api/station';
var API_BASE_URL = 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}';
var SWISS_CENTER = [46.799558, 8.235897];

var map = undefined;
var markers = undefined;

$(document).ready(() => {
    adaptContainerSizes();
    $(window).resize(() => adaptContainerSizes());
    
    $("input").checkboxradio();
    initializeMap(API_STATIONS_URL); 

    // filter by passenger frequency
    $('#slider-range').slider({
        range: true,    
        min: 0,
        max: 100,
        step: 10,
        orientation: 'horizantal',
        values: [ 0, 100 ],
        slide: function( event, ui ) {
            $("#slider-description-value").text(ui.values[0] + '% - ' + ui.values[1] + '%');
            updateMap(API_STATIONS_URL
                 + '?frequencyFrom=' + ui.values[0]
                 + '&frequencyTo=' + ui.values[1]);
        }
    });

    // filter by delay event binding
    $("#show-good, #show-avg, #show-bad").on('click', function(e) {
        updateMap(API_STATIONS_URL);
    });
});

/**
 * Adjusts container heights according window
 */
function adaptContainerSizes() {
    $('#map, #introduction').css('height', $(document).height());
}

/**
 * Reloads data from API and redraws the map
 * 
 * @param {targetUrl} targetUrl to load data from
 */
function updateMap(targetUrl) {
    try {
        map.removeLayer(markers);
        markers.clearLayers();
    } catch(e) {
    }
    markers = undefined;
    getNewData(targetUrl);
};

/**
 * Initializes the map with API data
 * 
 * @param {targetUrl} targetUrl to load data from
 */
function initializeMap(targetUrl) {
    map = L.map('map').setView(SWISS_CENTER, 8);

    L.tileLayer(API_BASE_URL, {
        attribution: 'ivisPro FS 18 Kevin Kirn und Ken Iseli',
        maxZoom: 18,
        id: 'mapbox.dark',
        accessToken: 'pk.eyJ1IjoiaWhrYXdpc3MiLCJhIjoiY2podWpyZ2VwMGw0ajNycWt3MGJldHhyOCJ9.q7dldOVKrTRX7Yxo4mlLtw'
    }).addTo(map);

    getNewData(targetUrl);
}

/**
 * Acquires train station data from API
 * 
 * @param {targetUrl} targetUrl to load data from
 */
function getNewData(targetUrl) {
    $.ajax({
        type: 'GET',
        async: true,
        url: targetUrl,
        contentType: 'application/json; charset=utf-8',
        success(response) {
            markers = L.markerClusterGroup({
                showCoverageOnHover: false,
                iconCreateFunction: function(cluster) {
                    let delays = getDelayRatioForCluster(cluster);
                    let colorClass = getDelayColorClass(delays);

                    return L.divIcon({ html: '<span>' + delays + '%</span>', className: 'clusterIcon ' + colorClass });
                }
            });

            response.forEach(element => {
                let showGood = $("#show-good").is(":checked");
                let showAvg = $("#show-avg").is(":checked");
                let showBad = $("#show-bad").is(":checked");
                
                if(!showAvg && element.delayRatio > 10 && element.delayRatio <= 30) {  // avg
                    return;
                } else if(!showBad && element.delayRatio > 30) { // bad
                    return;
                } else if(!showGood && element.delayRatio <= 10){ // ok
                    return;
                }
                
                markers.addLayer(
                    L.circle([element.location.longitude, element.location.latitude], {
                        color: getDelayColor(element.delayRatio),
                        fillColor: getDelayColor(element.delayRatio),
                        fillOpacity: 0.5,
                        opacity: 0.8,
                        radius: 500,
                        delayRatio: element.delayRatio
                    }).on('click', (event) => showDetailView(event, element))
                );
            });

            map.addLayer(markers);
        },
        error(response) {
            alert('Während dem Laden der Daten ist ein Fehler aufgetreten!');
        }
    });
};

/**
 * Calculates the number of delayed trains in the cluster.
 * 
 * @param {Cluster} cluster calculate delayed trains for 
 */
function getDelayRatioForCluster(cluster) {
    let children = cluster.getAllChildMarkers();
    let delayCount = 0;
    children.forEach(marker => {
        delayCount += marker.options.delayRatio;
    });

    return (delayCount / children.length).toFixed(0);
}

/**
 * Determines the appropriate color class.
 * 
 * @param {delayRatio} delayRatio 
 */
function getDelayColorClass(delayRatio) {
    let colorClass = 'green';
    if(delayRatio > 10) colorClass = 'orange';
    if(delayRatio > 30) colorClass = 'red';
    return colorClass;
}

/**
 * Determines the appropriate color class.
 * 
 * @param {delayRatio} delayRatio 
 */
function getDelayColor(delayRatio) {
    let colorClass = 'rgba(76, 175, 80, 0.7)';
    if(delayRatio > 10) colorClass = 'rgba(255, 152, 0, 0.7)';
    if(delayRatio > 30) colorClass = 'rgba(255, 0, 0, 0.7)';
    return colorClass;
}

/**
 * Shows the details view of a train station.
 * 
 * @param {event} event 
 * @param {element} element 
 */
function showDetailView(event, element){
    // remove previous installed container
    $('.detail-container').remove();

    let container = $('<div>', {class: 'detail-container'});
    
    // add close button
    let close = $('<div>', {class: 'close'});
    container.append(close);
    close.on('click', () => {
        container.animate({width: '0%', opacity: 0}, 500);
    });

    // add title
    let title = $('<span>', {class: 'title'});
    title.text(element.name);
    container.append(title);

    // add basic statistics
    let statisticParagraphs = $('<div>', {class: 'statistics-paragraph'});
    statisticParagraphs.append(p('Trains: ' + element.totalTrains));
    statisticParagraphs.append(p('On time: ' + element.trainsOnTime));
    statisticParagraphs.append(p('Delayed: ' + element.delayedTrains));
    container.append(statisticParagraphs);

    // add donut chart for delay metric
    let delayPercentageDonutContainer = $('<div>', {id: 'donut'});
    container.append(delayPercentageDonutContainer);

    // add donut chart for amount of different delay minutes metric
    let delayAmountsDonutContainer = $('<div>', {id: 'delay-amounts-donut'});
    container.append(delayAmountsDonutContainer);

    $('#map').append(container);

    // animation
    container.animate({width: '100%', opacity: 1, zIndex: 1100}, 500, () => {
        title.animate({opacity: 1}, 500);
        statisticParagraphs.animate({opacity: 1}, 500);
        drawDelayDonutChart(element.trainsOnTime, element.delayedTrains, delayPercentageDonutContainer);
        drawAmountOfDelaysChart(element.delays, delayAmountsDonutContainer);
    });
    
}

/**
 * Just wraps text into a <p> tag
 * 
 * @param {text} text 
 */
function p (text) {
    return '<span class="padded">' + text + '<span>';
}

/**
 * Draws a donut chart within the detail page
 * 
 * @param {onTime} onTime 
 * @param {delayed} delayed 
 * @param {target} target 
 */
function drawDelayDonutChart(onTime, delayed, target) {
    let chart = c3.generate({
        data: {
            columns: [
                ['On Time (' + onTime + ')', onTime],
                ['Delayed (' + delayed + ')', delayed],
            ],
            type : 'donut',
            onclick: function (d, i) { /*console.log("onclick", d, i);*/ },
            onmouseover: function (d, i) { /*console.log("onmouseover", d, i);*/ },
            onmouseout: function (d, i) { /*console.log("onmouseout", d, i);*/ }
        },
        donut: {
            title: "On time vs delay"
        }
    });

    target.append(chart.element);
}

/**
 * Draws a donut chart within the detail page
 * 
 * @param {data} data 
 * @param {target} target 
 */
function drawAmountOfDelaysChart(data, target) {
    var columns = [];
    for (let i = 0; i < data.length; i++) {
        columns.push([data[i].minutesDelay + ' minutes', data[i].occurrencesOfDelay]);
    }
    let title = 'Details to delays';
    if (columns.length === 0) {
        title = 'There are no delays';
    }
    let chart = c3.generate({
        data: {
            columns: columns,
            type : 'donut',
            onclick: function (d, i) { /*console.log("onclick", d, i);*/ },
            onmouseover: function (d, i) { /*console.log("onmouseover", d, i);*/ },
            onmouseout: function (d, i) { /*console.log("onmouseout", d, i);*/ }
        },
        donut: {
            title: title
        }
    });

    target.append(chart.element);
}
