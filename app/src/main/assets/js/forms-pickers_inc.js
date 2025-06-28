/**
 * Form Picker
 */

'use strict';

(function () {
  // Flat Picker
  // --------------------------------------------------------------------
  const flatpickrDateTime = document.querySelector('#flatpickr-datetime'),
		flatpickrDateTime2 = document.querySelector('#flatpickr-datetime2'),
		flatpickrDateTime3 = document.querySelector('#flatpickr-datetime3'),
		flatpickrDateTime4 = document.querySelector('#flatpickr-datetime4')
  
  // Datetime
  if (flatpickrDateTime) {
    flatpickrDateTime.flatpickr({
      enableTime: true,
      dateFormat: 'Y-m-d H:i'
    });
  }
 console.log(flatpickrDateTime2)
  // Datetime
  if (flatpickrDateTime2) {
    flatpickrDateTime2.flatpickr({
      enableTime: true,
      dateFormat: 'Y-m-d H:i'
    });
  }
	
  // Datetime
  if (flatpickrDateTime3) {
    flatpickrDateTime3.flatpickr({
      enableTime: true,
      dateFormat: 'Y-m-d H:i'
    });
  }

  // Datetime
  if (flatpickrDateTime4) {
    flatpickrDateTime4.flatpickr({
      enableTime: true,
      dateFormat: 'Y-m-d H:i'
    });
  }
	
})();
