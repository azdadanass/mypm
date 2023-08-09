$(function() {
	var availableTags = [ "Mhamed Bouougri", "Anass Azdad", "Asmae Abourhnim" ];
	function split(val) {
		return val.split(/@\s*/);
	}
	function extractLast(term) {
		if (split(term).length == 1)
			return "NaN";
		return (split(term)).pop();
	}

	// Overrides the default autocomplete filter function to search only from
	// the beginning of the string
	// $.ui.autocomplete.filter = function(array, term) {
	// var matcher = new RegExp("^" + $.ui.autocomplete.escapeRegex(term), "i");
	// return $.grep(array, function(value) {
	// return matcher.test(value.label || value.value || value);
	// });
	// };

	$(".autocompleteaa")
	// don't navigate away from the field on tab when selecting an item
	.on(
			"keydown",
			function(event) {
				if (event.keyCode === $.ui.keyCode.TAB
						&& $(this).autocomplete("instance").menu.active) {
					event.preventDefault();
				}
			}).autocomplete(
			{
				minLength : 0,
				source : function(request, response) {
					// delegate back to autocomplete, but extract the last term
					response($.ui.autocomplete.filter(availableTags,
							extractLast(request.term)));
				},
				focus : function() {
					// prevent value inserted on focus
					return false;
				},
				select : function(event, ui) {
					var terms = split(this.value);
					console.info(terms);
					// remove the current input
					terms.pop();
					// add the selected item
					terms.push("[" + ui.item.value + "]");
					// add placeholder to get the comma-and-space at the end
					terms.push("");
					this.value = terms.join(" ");
					return false;
				}
			});
});