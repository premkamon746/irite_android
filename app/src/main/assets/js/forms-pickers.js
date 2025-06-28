/**
 * Form Picker
 */

'use strict';

(function () {
 
  flatpickr("#incident_create_datetime", {
      dateFormat: "d F Y H:i น",
      locale: {
        weekdays: {
          shorthand: ['อา.', 'จ.', 'อ.', 'พ.', 'พฤ.', 'ศ.', 'ส.'],
          longhand: ['อาทิตย์', 'จันทร์', 'อังคาร', 'พุธ', 'พฤหัสบดี', 'ศุกร์', 'เสาร์'],
        },
        months: {
          shorthand: ['ม.ค.', 'ก.พ.', 'มี.ค.', 'เม.ย.', 'พ.ค.', 'มิ.ย.', 'ก.ค.', 'ส.ค.', 'ก.ย.', 'ต.ค.', 'พ.ย.', 'ธ.ค.'],
          longhand: ['มกราคม', 'กุมภาพันธ์', 'มีนาคม', 'เมษายน', 'พฤษภาคม', 'มิถุนายน', 'กรกฎาคม', 'สิงหาคม', 'กันยายน', 'ตุลาคม', 'พฤศจิกายน', 'ธันวาคม'],
        },
        firstDayOfWeek: 1,
      },
      enableTime: true,
      time_24hr: true,
      onChange: function(selectedDates, dateStr, instance) {
        console.log(dateStr)
        const [day, month, year, time] = dateStr.replace(' น', '').split(' ');
        const gregorianYear = parseInt(year);
        const monthIndex = {
          'มกราคม': '01', 'กุมภาพันธ์': '02', 'มีนาคม': '03', 'เมษายน': '04',
          'พฤษภาคม': '05', 'มิถุนายน': '06', 'กรกฎาคม': '07', 'สิงหาคม': '08',
          'กันยายน': '09', 'ตุลาคม': '10', 'พฤศจิกายน': '11', 'ธันวาคม': '12'
        }[month];

        const formattedDate = `${day}/${monthIndex}/${gregorianYear}, ${time.trim()}`;
        //console.log(formattedDate)
      }
  });

   flatpickr("#incident_report_known_datetime", {
      dateFormat: "d F Y H:i น",
      locale: {
        weekdays: {
          shorthand: ['อา.', 'จ.', 'อ.', 'พ.', 'พฤ.', 'ศ.', 'ส.'],
          longhand: ['อาทิตย์', 'จันทร์', 'อังคาร', 'พุธ', 'พฤหัสบดี', 'ศุกร์', 'เสาร์'],
        },
        months: {
          shorthand: ['ม.ค.', 'ก.พ.', 'มี.ค.', 'เม.ย.', 'พ.ค.', 'มิ.ย.', 'ก.ค.', 'ส.ค.', 'ก.ย.', 'ต.ค.', 'พ.ย.', 'ธ.ค.'],
          longhand: ['มกราคม', 'กุมภาพันธ์', 'มีนาคม', 'เมษายน', 'พฤษภาคม', 'มิถุนายน', 'กรกฎาคม', 'สิงหาคม', 'กันยายน', 'ตุลาคม', 'พฤศจิกายน', 'ธันวาคม'],
        },
        firstDayOfWeek: 1,
      },
      enableTime: true,
      time_24hr: true,
      onChange: function(selectedDates, dateStr, instance) {
        console.log(dateStr)
        const [day, month, year, time] = dateStr.replace(' น', '').split(' ');
        const gregorianYear = parseInt(year);
        const monthIndex = {
          'มกราคม': '01', 'กุมภาพันธ์': '02', 'มีนาคม': '03', 'เมษายน': '04',
          'พฤษภาคม': '05', 'มิถุนายน': '06', 'กรกฎาคม': '07', 'สิงหาคม': '08',
          'กันยายน': '09', 'ตุลาคม': '10', 'พฤศจิกายน': '11', 'ธันวาคม': '12'
        }[month];

        const formattedDate = `${day}/${monthIndex}/${gregorianYear}, ${time.trim()}`;
        //console.log(formattedDate)
        $("#incident_report_known_datetime").val(formattedDate)
      }
  });


})();



function getCurrentThaiFormattedDate() {
    const monthNames = {
      '01': 'มกราคม',
      '02': 'กุมภาพันธ์',
      '03': 'มีนาคม',
      '04': 'เมษายน',
      '05': 'พฤษภาคม',
      '06': 'มิถุนายน',
      '07': 'กรกฎาคม',
      '08': 'สิงหาคม',
      '09': 'กันยายน',
      '10': 'ตุลาคม',
      '11': 'พฤศจิกายน',
      '12': 'ธันวาคม'
    };

    const now = new Date();
    const day = String(now.getDate()).padStart(2, '0');
    const month = monthNames[String(now.getMonth() + 1).padStart(2, '0')];
    const year = now.getFullYear();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');

    return `${day} ${month} ${year} ${hours}:${minutes} น`;
}
