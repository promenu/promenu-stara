//Communicate with Javascript Interface
var clickFunction = function() {
    var restaurant_name = this.getAttribute("data-restaurant_name");

    var slug = this.getAttribute("data-slug");

    var mobile_id = this.getAttribute("data-mobile_id");

    var latitude = this.getAttribute("data-latitude");

    var longitude = this.getAttribute("data-longitude");

    if(window.JSInterface){
    	    window.JSInterface.detail(restaurant_name, slug, mobile_id, latitude, longitude);
    }
};
/*
var searchFunction = function() {
    var search_value = getElementById("search_value").value;

    if(window.JSInterface){
        window.JSInterface.search(search_value);
    }

    return false;
};*/

function searchFunction() {

    var search_value = document.getElementById("search_value").value;

    if(window.JSInterface){
        window.JSInterface.search(search_value);
    }

    document.activeElement.blur();

    return false;
}

document.addEventListener("DOMContentLoaded", function(event) {
    //var form_element = getElementById("search_form");

    //form_element.addEventListener("submit", searchFunction, false);

    var classname = document.getElementsByClassName("restaurant-box");

    for (var i = 0; i < classname.length; i++) {
        classname[i].addEventListener('click', clickFunction, false);
    }
});