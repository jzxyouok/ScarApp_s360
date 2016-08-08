/*
	Author: Miaohu
	Description: This is the list.js.
	Date: 1/13/2015
*/
var OFFSET = 5;
var page = 1;
var PAGESIZE = 10;

var myScroll,
	pullDownEl, pullDownOffset,
	pullUpEl, pullUpOffset,
	generatedCount = 0;
var maxScrollY = 0;

var hasMoreData = false;

document.addEventListener('touchmove', function(e) {
	e.preventDefault();
}, false);

document.addEventListener('DOMContentLoaded', function() {
	$(document).ready(function() {
		loaded();
	});
}, false);
var pullDownEl ;
var pullDownOffset;
var pullUpEl;
var pullUpOffset;

var hasMoreData = false;

function loaded() {
	pullDownEl = document.getElementById('pullDown');
	pullDownOffset = pullDownEl.offsetHeight;
	pullUpEl = document.getElementById('pullUp');
	pullUpOffset = pullUpEl.offsetHeight;
	// $("#thelist").hide();
	$("#pullUp").hide();
	myScroll = new iScroll('wrapper', {
		useTransition: true,
		topOffset: pullDownOffset,
		onRefresh: function() {
			if (pullDownEl.className.match('loading')) {
				pullDownEl.className = 'idle';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '涓嬫媺鍒锋柊...';
				this.minScrollY = -pullDownOffset;
			}
			if (pullUpEl.className.match('loading')) {
				pullUpEl.className = 'idle';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '涓婃媺鍔犺浇鏇村...';
			}
		},
		onScrollMove: function() {
			if (this.y > OFFSET && !pullDownEl.className.match('flip')) {
				pullDownEl.className = 'flip';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '閲婃斁鍒锋柊...';
				this.minScrollY = 0;
			} else if (this.y < OFFSET && pullDownEl.className.match('flip')) {
				pullDownEl.className = 'idle';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '涓嬫媺鍒锋柊...';
				this.minScrollY = -pullDownOffset;
			} 
			if (this.y < (maxScrollY - pullUpOffset - OFFSET) && !pullUpEl.className.match('flip')) {
				if (hasMoreData) {
					this.maxScrollY = this.maxScrollY - pullUpOffset;
					pullUpEl.className = 'flip';
					pullUpEl.querySelector('.pullUpLabel').innerHTML = '閲婃斁鍒锋柊...';
				}
			} else if (this.y > (maxScrollY - pullUpOffset - OFFSET) && pullUpEl.className.match('flip')) {
				if (hasMoreData) {
					this.maxScrollY = maxScrollY;
					pullUpEl.className = 'idle';
					pullUpEl.querySelector('.pullUpLabel').innerHTML = '涓婃媺鍔犺浇鏇村...';
				}
			}
		},
		onScrollEnd: function() {
			if (pullDownEl.className.match('flip')) {
				pullDownEl.className = 'loading';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Loading...';
				// pullDownAction(); // Execute custom function (ajax call?)
				refresh();
			}
			if (hasMoreData && pullUpEl.className.match('flip')) {
				pullUpEl.className = 'loading';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Loading...';
				// pullUpAction(); // Execute custom function (ajax call?)
				nextPage();
			}
		}
	});
	pullDownEl.className = 'loading';
	pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Loading...';
	
	get_data(1);
}

function refresh() {
	page = 1;
	get_data(page);
}

function nextPage() {
	page++;
	get_data(page);
}