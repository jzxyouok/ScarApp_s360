/**
 * 菜单操作类
 */
(function ($) {
	
	fts.menu = function () {};
	
	$.extend(fts.menu.prototype, {
		
		liSelect : '',
		
		activeClass : '',
		
		_findMenuByIndex : function (idx) {	
			
			return $(this.liSelect).eq(idx);
		},
		
		activeMenu : function (idx) {
			var currMenu = this._findMenuByIndex(idx);
			var className = currMenu.attr('class');
			currMenu.removeClass(className);
			var activeClass = className.substring(0, className.length - 1);
			currMenu.addClass(activeClass);
		}
		
	});
	
})(jQuery);