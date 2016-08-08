/*
 * 定义fts对象
 */
(function (win, $) {
	
	if(!win.fts)
		win.fts = {};

	win.fts = {
		baseURI : ''
	};

	fts.event = {
		imgError : function (querySeleter) {
			$(querySeleter).error(function () {
				$(this).attr('src', fts.baseURI + 'res/images/default.jpg');
			});
		}
	};
	
	/*-----------------------------------------工具方法----------------------------------------------*/
	/*
	 * 定时显示时间
	 */
	fts.timing = function (/*number 毫秒数*/time, /*String 父节点ID*/elementId) {
		var _t = this;
		var minutes;
		var seconds;
		var showText = '';
		
		var _time = parseFloat(time) / 1000;
		var _element = $(elementId);
		var _a = _element.next();
		
		if(_time && _time > 0) 
		{
			if(_time > 60) 
			{
				minutes = parseInt(_time / 60);
				if(minutes && minutes > 0) 
				{
					showText = minutes + '分';
				}
				
				seconds = parseInt((parseFloat(_time /60.0) - parseInt(_time /60.0)) * 60);
				if(seconds && seconds > 0) 
				{
					showText += ' ' + seconds + '秒';
				}
			}
			else
			{
				showText = parseInt(_time) + '秒';
			}
		}
		if(_time == 0) 
		{
			showText = '重新获取';
			_element.html('');
			_a.html(showText);
		}
		else 
		{
			setTimeout(function() {
				_t.timing(time - 1000, elementId);
			},1000);
			_a.html('');
			_element.html(showText);
		}
	}
	
	fts.format = function (number, form) {
	    var forms = form.split('.'), number = '' + number, numbers = number.split('.'), leftnumber = numbers[0].split(''),
	    exec = function (lastMatch) {
	    	if (lastMatch == '0' || lastMatch == '#') {
	    		if (leftnumber.length) {
	    			return leftnumber.pop();
	    		} else if (lastMatch == '0') {
	    			return lastMatch;
	    		} else {
	    			return '';
	    		}
	    	} else {
	    		return lastMatch;
	    	}
	    },
	    string = forms[0].split('').reverse().join('').replace(/./g, exec).split('').reverse().join('');
	    string = leftnumber.join('') + string;
	    if (forms[1] && forms[1].length) {
		    leftnumber = (numbers[1] && numbers[1].length) ? numbers[1].split('').reverse() : [];
		    string += '.' + forms[1].replace(/./g, exec);
	    }
	    return string;
	};
		
	return fts;
	
})(window, jQuery);
