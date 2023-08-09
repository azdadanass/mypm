var oldie = /msie\s*(8|7|6)/.test(navigator.userAgent.toLowerCase());
$('.easy-pie-chart.percentage')
		.each(
				function() {
					var $box = $(this).closest('.infobox');
					var barColor = $(this).data('color')
							|| (!$box.hasClass('infobox-dark') ? $box
									.css('color') : 'rgba(255,255,255,0.95)');
					var trackColor = barColor == 'rgba(255,255,255,0.95)' ? 'rgba(255,255,255,0.25)'
							: '#E2E2E2';
					var size = parseInt($(this).data('size')) || 50;
					$(this)
							.easyPieChart(
									{
										barColor : barColor,
										trackColor : trackColor,
										scaleColor : false,
										lineCap : 'butt',
										lineWidth : parseInt(size / 10),
										animate : /msie\s*(8|7|6)/
												.test(navigator.userAgent
														.toLowerCase()) ? false
												: 1000,
										size : size
									});
				}

		);