
function addGML() {
	vectors = new OpenLayers.Layer.Vector("VectorLayer");
    map.addLayer(vectors);
    txt_gml = '<?xml version="1.0" encoding="ISO-8859-1"?><wfs:FeatureCollection xmlns:ms="http://mapserver.gis.umn.edu/mapserver" xmlns:wfs="http://www.opengis.net/wfs" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd"><gml:boundedBy><gml:Box srsName="EPSG:32628"><gml:coordinates>370523.46875,3145634 370695.78125,3146325.75</gml:coordinates></gml:Box></gml:boundedBy><gml:featureMember><ms:capa fid="1"><ms:Nombre>Calle Chineguas</ms:Nombre><ms:Clasificacion>Calle Chineguas</ms:Clasificacion><ms:Localizacion>SANTA CRUZ DE TENERIFE Tenerife</ms:Localizacion><ms:Imagen>None</ms:Imagen><ms:msGeometry><gml:MultiLineString srsName="EPSG:32628"><gml:lineStringMember><gml:LineString><gml:coordinates>370695.7566,3146325.5417 370668.1198,3146214.5844 370638.9055,3146094.8577 370609.685,3145975.5561 370609.01,3145972.8003 370579.9133,3145856.8967 370549.5427,3145740.9143 370523.4765,3145634.0191</gml:coordinates></gml:LineString></gml:lineStringMember></gml:MultiLineString></ms:msGeometry></ms:capa></gml:featureMember></wfs:FeatureCollection>';
    var features = new OpenLayers.Format.GML().read(txt_gml);
    var bounds;
    if(features) {
        if(features.constructor != Array) {
            features = [features];
        }
        for(var i=0; i<features.length; ++i) {
            if (!bounds) {
                bounds = features[i].geometry.getBounds();
            } else {
                bounds.extend(features[i].geometry.getBounds());
            }
        }
        vectors.addFeatures(features);
        map.zoomToExtent(bounds);
    }	
}

// initialize map when page ready
var map;

// Get rid of address bar on iphone/ipod
var fixSize = function() {
    window.scrollTo(0,0);
    document.body.style.height = '100%';
    if (!(/(iphone|ipod)/.test(navigator.userAgent.toLowerCase()))) {
        if (document.body.parentNode) {
            document.body.parentNode.style.height = '100%';
        }
    }
};
setTimeout(fixSize, 700);
setTimeout(fixSize, 1500);

var init = function () {

    // create map
    map = new OpenLayers.Map({
        div: "map",
        theme: null,
        maxExtent: new OpenLayers.Bounds(189989.781, 3061640.75,652834.562, 3252975.75),
        maxResolution: "auto",
        projection: "EPSG:32628",
        units: 'm',
        controls: [
            new OpenLayers.Control.Attribution(),
            new OpenLayers.Control.TouchNavigation({
                dragPanOptions: {
                    enableKinetic: true
                }
            }),
            new OpenLayers.Control.ZoomPanel(),
            new OpenLayers.Control.PinchZoom()
        ],
        layers: [
            new OpenLayers.Layer.WMS("Orto", "http://idecan1.grafcan.es/ServicioWMS/OrtoExpress?",
            		 {layers: 'ortoexpress', format: 'image/jpeg'}
            )
        ],
        center: new OpenLayers.LonLat(376166.08, 3149156.70),
        zoom: 3
    });

    //map.zoomToMaxExtent();
    
    console.log("Init");
    
    /*
    vectors = new OpenLayers.Layer.Vector("VectorLayer");
    map.addLayer(vectors);
    txt_gml = '<?xml version="1.0" encoding="ISO-8859-1"?><wfs:FeatureCollection xmlns:ms="http://mapserver.gis.umn.edu/mapserver" xmlns:wfs="http://www.opengis.net/wfs" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd"><gml:boundedBy><gml:Box srsName="EPSG:32628"><gml:coordinates>370523.46875,3145634 370695.78125,3146325.75</gml:coordinates></gml:Box></gml:boundedBy><gml:featureMember><ms:capa fid="1"><ms:Nombre>Calle Chineguas</ms:Nombre><ms:Clasificacion>Calle Chineguas</ms:Clasificacion><ms:Localizacion>SANTA CRUZ DE TENERIFE Tenerife</ms:Localizacion><ms:Imagen>None</ms:Imagen><ms:msGeometry><gml:MultiLineString srsName="EPSG:32628"><gml:lineStringMember><gml:LineString><gml:coordinates>370695.7566,3146325.5417 370668.1198,3146214.5844 370638.9055,3146094.8577 370609.685,3145975.5561 370609.01,3145972.8003 370579.9133,3145856.8967 370549.5427,3145740.9143 370523.4765,3145634.0191</gml:coordinates></gml:LineString></gml:lineStringMember></gml:MultiLineString></ms:msGeometry></ms:capa></gml:featureMember></wfs:FeatureCollection>';
    var features = new OpenLayers.Format.GML().read(txt_gml);
    var bounds;
    if(features) {
        if(features.constructor != Array) {
            features = [features];
        }
        for(var i=0; i<features.length; ++i) {
            if (!bounds) {
                bounds = features[i].geometry.getBounds();
            } else {
                bounds.extend(features[i].geometry.getBounds());
            }
        }
        vectors.addFeatures(features);
        map.zoomToExtent(bounds);
    }
    */

    //alert("Hola");
    //AndroidFunction.showToast("Cargado...");
    
};

