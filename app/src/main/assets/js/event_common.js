

function isJsonArrayString(str) {
  try {
    const parsed = JSON.parse(str);
    return Array.isArray(parsed);
  } catch (e) {
    return false;
  }
}


function submitForm(jsonObject) {
  //console.log(jsonObject)
  Android.submitForm(JSON.stringify(jsonObject));
}

function isMobileDevice() {
  return /Mobi|Android|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

function getAllField(id) {
  let formDataArray = $(id).serializeArray(); // Only checked checkboxes and filled fields
  let formDataConv = formDataArray.filter(field => field.value.trim() !== "");

  const $form = $(id);

  // 1. Detect all checkboxes in the form
  const allCheckboxes = $form.find('input[type="checkbox"]');

  allCheckboxes.each(function () {
    const checkboxName = this.name;

    // Skip if already included (i.e. it's checked and present in formDataArray)
    const isAlreadyIncluded = formDataConv.some(field => field.name === checkboxName);

    if (!isAlreadyIncluded) {
      formDataConv.push({ name: checkboxName, value: "" }); // unchecked → false
    }
  });

  // 2. Convert to structured object
  let formDataObj = formDataConv.reduce((obj, item) => {
    const value = item.value === "on" ? true : item.value;

    if (item.name.endsWith('[]')) {
      const arrayKey = item.name.slice(0, -2);
      if (!obj[arrayKey]) {
        obj[arrayKey] = [];
      }
      obj[arrayKey].push(value);
    } else {
      obj[item.name] = value;
    }

    return obj;
  }, {});

  return formDataObj;
}

var formData = '{"name":"Alice","age":30,"email":"alice@example.com"}';
 if (isMobileDevice()) {
    formData = Android.getJsonData("","getFormData","");
 }
 console.log(formData)
 jsonObject = typeof formData === "string" ? JSON.parse(formData) : formData;


function saveListData(index, data, ListDataArray, field) {
  ListDataArray[index] = data;
  data_send = {
    "button":"update_list",
    "field":field,
    "data":ListDataArray
  }
  var jsondata = JSON.stringify(data_send)
  if (isMobileDevice()) {
    submitForm(data_send)
  } else {
    console.log(data_send)
  }
}



function checkAndAddNewRow(rows, id, callback) {
  const $rows = $(rows);
  const $lastRow = $rows.last();
  const hasInput = $lastRow.find('input').toArray().some(input => $(input).val().trim() !== '');

  if (hasInput) {
    $(id).append(callback());
  }
}

function renderForm(dataList,id,callback) {
  const $container = $(id);
  $container.empty();
  dataList.forEach(data => {
    $container.append(callback(data));
  });
  $container.append(callback()); // เพิ่มบรรทัดว่างไว้ 1 บรรทัด
}   


$(document).ready(function () {
    /*$('#assetForm input, #assetForm textarea, #assetForm select').on('blur', function () {
        const fieldName = $(this).attr('name');
        var fieldValue = $(this).val();

        var json = {
              "fieldName":fieldName,
              "fieldValue":fieldValue,
              "button":"single"
          }

          if (isMobileDevice()) {
            submitForm(json)
          } else {
            console.log("Desktop detected");
          }
      });

      $('.btn-next').on('click', function () {
            let formDataArray = $("#assetForm").serializeArray(); // [{ name: 'key', value: 'val' }, ...]

            let formDataConv = formDataArray.filter(field => field.value.trim() !== "");
            let formDataObj = formDataConv.reduce((obj, item) => {
                // Check if the field name ends with "[]", indicating it's an array
                if (item.name.endsWith('[]')) {
                    // Remove "[]" from the name to get the array key
                    const arrayKey = item.name.slice(0, -2);
                    
                    // Initialize the array if it doesn't exist yet
                    if (!obj[arrayKey]) {
                        obj[arrayKey] = [];
                    }
                    
                    // Push the value into the corresponding array
                    obj[arrayKey].push(item.value);
                } else {
                    // Otherwise, just set the key-value pair in the object
                    obj[item.name] = item.value;
                }
                return obj;
            }, {});

            formDataObj["button"] = "multiple"
            console.log(formDataObj["button"])
            if (isMobileDevice()) {
              submitForm(formDataObj)
            } else {
              console.log("Desktop detected");
            }
        });*/

  // Handle text, textarea, number, etc.

    function sendAll(){
      var object = getAllField("#allForm")
      object.button = "multiple"
      if (isMobileDevice()) {
        submitForm(object)
      } else {
        console.log(object)
      }
    }
      let timer;
      $('input:not([type=checkbox]):not([type=radio]), textarea,.checkAll').on('input', function () {
            clearTimeout(timer);
            const $el = $(this);
            const key = this.id;
            const val = $el.val();
            timer = setTimeout(function () {
              /*if (key) {
                console.log(key + ' ' + val);
              }*/
              sendAll()

            }, 500);
          });

          // Handle checkbox and radio separately
          $('input[type=checkbox], input[type=radio]').on('change', function () {
                const $el = $(this);
                const key = this.id;
                let input_type = $el.prop("type");
                sendAll()
          });

    
    $(function() {
      const today = new Date();
      const dd = String(today.getDate()).padStart(2, '0');
      const mm = String(today.getMonth() + 1).padStart(2, '0'); // Months are 0-based
      const yyyy = today.getFullYear();
      const formatted = `${dd}/${mm}/${yyyy}`;

      $('.date_picker').val(formatted);
    });

    $(document).on('click', '.remove-owner', function () {
      wl = $('.remove-owner').length
      if (wl > 2)
      {
        $(this).closest('.owner-row').remove();
      }
      else
      {
        $(this).closest('.owner-row').val("");
      }
    });


        

        $.each(jsonObject, function(key, value) {
          var stringValue = String(value).replace(/"/g, '');

          let inputSelector = $('[name="' + key + '"], [name="' + key + '[]"]'); // match both single and array
          let input_type = inputSelector.prop("type");
          /*if (isJsonArrayString(value) ){
            try {
              value = JSON.parse(value);
            } catch (e) {
              console.error("Invalid JSON:", e);
            }
            $('#owner-list').empty();
            $.each(value, function(i, val) {
              $('#owner-list').append(`<div class="d-flex mb-2 align-items-center owner-row">
                <input class="form-control form-control-sm" type="text" value="${val}" placeholder="ระบุผู้ตรวจสถานที่เกิดเหตุ (ถ้ามี)" name="owner_list[]"/>
                <button type="button" class="btn btn-sm btn-danger remove-owner">ลบ</button>
              </div>
              `);
            });

            $('#owner-list').append(`
                <div class="d-flex mb-2 align-items-center owner-row">
                <input class="form-control form-control-sm" type="text" placeholder="ระบุผู้ตรวจสถานที่เกิดเหตุ (ถ้ามี)" name="owner_list[]" />
                <button type="button" class="btn btn-sm btn-danger remove-owner">ลบ</button>
              </div>
              `);
          }else */
          if(input_type == "checkbox")
          {
            try {
              const arrValue = JSON.parse(value);
                $('input[name="' + key + '[]"]').each(function() {
                    $(this).prop('checked', arrValue.includes($(this).val()));
                });
            } catch (error) {
            }
                
          }else if(input_type == "radio")
          {
             $('input[name="'+key+'"][value="' + stringValue + '"]').prop('checked', true);
          }
          else if(input_type == "textarea")
          {
             $('textarea[name="'+key+'"]').val(stringValue);
          }
          else if(input_type == "text" && $('input[type="text"]:not(.dynamic)'))
          {
            $('input[name="'+key+'"]').val(stringValue);
          }

        });

        flatpickr(".date_picker", {
          dateFormat: "d/m/Y",
          allowInput: false,
          disableMobile: true  
        });

        flatpickr(".timepicker", {
            enableTime: true,
            noCalendar: true,
            time_24hr: true,
            allowInput: false,
            disableMobile: true,
            dateFormat: "H:i", 
            /*onChange: function(selectedDates, dateStr, instance) {
              instance.close(); // ✅ Auto-close after selecting time
            }*/
        });

        

        $('#owner-list').on('input', 'input', function () {
          const $inputs = $('#owner-list input');
          const $this = $(this);
          const isLast = $this.is($inputs.last());

          // Add new input if last input is not empty
          if ($this.val() && isLast) {
            $('#owner-list').append(`<div class="d-flex mb-2 align-items-center owner-row">
                <input class="form-control form-control-sm" type="text" placeholder="ระบุผู้ตรวจสถานที่เกิดเหตุ (ถ้ามี)" name="owner_list[]" />
                <button type="button" class="btn btn-sm btn-danger remove-owner">ลบ</button>
              </div>`
            );
          }

          // Remove extra empty inputs (if two empty in a row)
          $inputs.each(function (i) {
            if (i < $inputs.length - 1) {
              const $current = $($inputs[i]);
              const $next = $($inputs[i + 1]);
              if (!$current.val() && !$next.val()) {
                $next.remove();
                return false; // Exit loop after removing one
              }
            }
          });
        });


    var owner_list = []
    if (isMobileDevice()) {
      try {
        
        owner_list = JSON.parse(jsonObject.owner_list).filter(item => item !== null);
      } catch (e) {
         owner_list = []
      } 
    }

    function createOwnerRow(data = {}) {
      const $row = $(`
        <div class="row align-items-end mb-3 owner-row">
          <div class="col-9 col-sm-10 col-md-6 col-lg-4 col-xl-4">
            <input class="form-control form-control-sm owner" type="text" placeholder="ระบุผู้ตรวจสถานที่เกิดเหตุ (ถ้ามี)" name="owner_list[]" />
          </div>
          <div class="col-12 col-sm-2 col-md-2 col-lg-2 col-xl-2 text-end">
            <button type="button" class="btn btn-danger btn-sm btn-remove">ลบ</button>
          </div>
        </div>
      `);

      // กรอกข้อมูลจาก JSON ถ้ามี
      $row.find('.owner').val(data.name || '');

      // ตรวจสอบการเปลี่ยน input แล้วเพิ่มแถว
      $row.find('input').on('input', function () {
        checkAndAddNewRow('.owner-row', '#owner_list',createOwnerRow);
      });

      // ลบแถว
      $row.find('.btn-remove').on('click', function () {
        const $rows = $('.owner-row');
        if ($rows.length > 1) {
          $row.remove();
          const index = $row.index();
          owner_list.splice(index, 1);
          sharedHandler()
        } else {
          // ถ้าเหลือแถวเดียว ให้ล้างค่าแทน
          $row.find('input').val('');
        }
      });


      let saveTimeout;
      $row.find('input').on('input blur',sharedHandler)
      function sharedHandler(event){
        clearTimeout(saveTimeout);
        saveTimeout = setTimeout(() => {
          const updatedData = {
            name: $row.find('.owner').val(),
          };
          //console.log('Auto-saving victim data:', updatedData);
          saveListData($row.index(), updatedData, owner_list , "owner_list" );
        }, 500); // Save after 300ms pause
      } 
      return $row;
    }

    $(document).ready(function () {
      renderForm(owner_list,'#owner_list',createOwnerRow);
    });

});




         