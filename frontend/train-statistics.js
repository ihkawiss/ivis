var map = L.map('map').setView([46.799558, 8.235897], 8);

L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
    attribution: 'ivisPro FS 18 Kevin Kirn und Ken Iseli',
    maxZoom: 18,
    id: 'mapbox.dark',
    accessToken: 'pk.eyJ1IjoiaWhrYXdpc3MiLCJhIjoiY2podWpyZ2VwMGw0ajNycWt3MGJldHhyOCJ9.q7dldOVKrTRX7Yxo4mlLtw'
}).addTo(map);

$.ajax({
    type: 'GET',
    async: true,
    url: "http://localhost:8080/api/station",
    contentType: "application/json; charset=utf-8",
    success(response) {
        var markers = L.markerClusterGroup({
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
                })
            );
        });

        map.addLayer(markers);
    },
    error(response) {
        alert("Während dem Laden der Daten ist ein Fehler aufgetreten!");
    }
})

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