jQuery(function($) {
	$('[data-rel=tooltip]').tooltip();
	$('.aa-tooltip').tooltip();
	$('[data-rel=popover]').popover({
		html : true
	});
});