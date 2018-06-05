var API_BASE_URL = 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}';
var SWISS_CENTER = [46.799558, 8.235897];

var map = undefined;
var markers = undefined;

$(document).ready(() => {
    $("input").checkboxradio();
    adaptContainerSizes();
    initializeMap('http://localhost:8080/api/station');

    $(window).resize(() => adaptContainerSizes());

    $('#slider-range').slider({
        range: true,    
        min: 0,
        max: 100,
        step: 10,
        orientation: 'horizantal',
        values: [ 0, 100 ],
        slide: function( event, ui ) {
            $("#slider-description-value").text(ui.values[0] + '% - ' + ui.values[1] + '%');
            updateMap('http://localhost:8080/api/station?'
                 + 'frequencyFrom=' + ui.values[0]
                 + '&frequencyTo=' + ui.values[1]);
        }
    });
});

function adaptContainerSizes() {
    $('#map, #introduction').css('height', $(document).height());
}

function updateMap(targetUrl) {
    try {
        map.removeLayer(markers);
        markers.clearLayers();
    } catch(e) {
    }
    markers = undefined;
    getNewData(targetUrl);
};

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
    let donutContainer = $('<div>', {id: 'donut'});
    container.append(donutContainer);

    $('#map').append(container);

    // animation
    container.animate({width: '100%', opacity: 1, zIndex: 1100}, 500, () => {
        title.animate({opacity: 1}, 500);
        statisticParagraphs.animate({opacity: 1}, 500);
        drawDelayDonutChart(element.totalTrains, element.trainsOnTime, donutContainer);
    });
    
}

/**
 * Just wraps text into a <p> tag
 * 
 * @param {text} text 
 */
function p (text) {
    return '<p>' + text + '<p>';
}

function drawDelayDonutChart(onTime, delayed, target) {
    let chart = c3.generate({
        data: {
            columns: [
                ['On Time (' + onTime + ')', onTime],
                ['Delayed (' + delayed + ')', delayed],
            ],
            type : 'donut',
            onclick: function (d, i) { console.log("onclick", d, i); },
            onmouseover: function (d, i) { console.log("onmouseover", d, i); },
            onmouseout: function (d, i) { console.log("onmouseout", d, i); }
        },
        donut: {
            title: "Train Statistics"
        }
    });

    target.append(chart.element);
}