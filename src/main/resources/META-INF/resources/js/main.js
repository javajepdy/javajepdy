var els=document.getElementsByClassName('banner');

Array.prototype.forEach.call(els, function(el) {
    new CircleType(el).radius(600);
});