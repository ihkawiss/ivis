var map = L.map('map').setView([46.799558, 8.235897], 8);

L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
    attribution: 'ivisPro FS 18 Kevin Kirn und Ken Iseli',
    maxZoom: 18,
    id: 'mapbox.light',
    accessToken: 'pk.eyJ1IjoiaWhrYXdpc3MiLCJhIjoiY2podWpyZ2VwMGw0ajNycWt3MGJldHhyOCJ9.q7dldOVKrTRX7Yxo4mlLtw'
}).addTo(map);

$.ajax({
    type: 'GET',
    async: true,
    url: "http://localhost:8080/api/station",
    contentType: "application/json; charset=utf-8",
    success(response) {
        var markers = L.markerClusterGroup();
        response.forEach(element => {
            markers.addLayer(
                L.circle([element.location.longitude, element.location.latitude], {
                    color: element.color,
                    fillColor: element.color,
                    fillOpacity: 0.5,
                    opacity: 0.5,
                    radius: 500
                })
            );
        });

        map.addLayer(markers);
    },
    error(response) {
        alert("Während dem Laden der Daten ist ein Fehler aufgetreten!");
    }
})

longitude
