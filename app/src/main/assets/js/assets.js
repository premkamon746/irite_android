var deceasedData = [];
    if (isMobileDevice()) {
      try {
        deceasedData = JSON.parse(jsonObject.victim_name);
      } catch (e) {
         deceasedData = []
      } 
    }

    function createDeceasedRow(data = {}) {
      const $row = $(`
        <div class="row pr-3 pl-3 align-items-end mb-3 pain-row">
          <div class="col-9 col-sm-10 col-md-6 col-lg-4 col-xl-4 mb-3">
            <label class="form-label" style="font-size: 16px">ชื่อผู้เสียชีวิต/บาดเจ็บ</label>
            <input type="text" class="form-control form-control-sm deceased-name" placeholder="ระบุชื่อ - นามสกุล ผู้เสียหาย">
          </div>
          <div class="col-3 col-sm-2 col-md-2 col-lg-2 col-xl-2 mb-3">
            <label class="form-label" style="font-size: 16px">อายุ (ปี)</label>
            <input type="number" class="form-control form-control-sm deceased-age" min="0" placeholder="ระบุอายุ">
          </div>
          <div class="col-2 col-sm-2 col-md-2 col-lg-2 col-xl-2 text-end mb-2">
            <button type="button" class="btn btn-danger btn-sm btn-remove">ลบ</button>
          </div>
        </div>
      `);

      // ใส่ข้อมูลถ้ามี
      $row.find('.deceased-name').val(data.name || '');
      $row.find('.deceased-age').val(data.age || '');

      

      // เพิ่มแถวใหม่ถ้ามีการกรอก
      $row.find('input').on('input', function () {
        checkAndAddNewRow('.pain-row', '#deceased-form-container',createDeceasedRow);
      });

      // ปุ่มลบ
      $row.find('.btn-remove').on('click', function () {
        const $rows = $('.pain-row');
        if ($rows.length > 1) {
          $row.remove();
          const index = $row.index();
          deceasedData.splice(index, 1);
          sharedHandler()
        } else {
          $row.find('input').val('');
        }
      });

      let saveTimeout;
      $row.find('input').on('input blur', sharedHandler);

      function sharedHandler(event) {
        clearTimeout(saveTimeout);
        saveTimeout = setTimeout(() => {
          const updatedData = {
            name: $row.find('.deceased-name').val(),
            age: $row.find('.deceased-age').val()
          };
          //console.log('Auto-saving victim data:', updatedData);
          saveListData($row.index(), updatedData, deceasedData , "victim_name" );
        }, 500); // Save after 300ms pause
      }

      return $row;
    }

    var victimData = []
    if (isMobileDevice()) {
      try {
        victimData = JSON.parse(jsonObject.victim_name);
      } catch (e) {
         victimData = []
      } 
    }

// เริ่มโหลด
$(document).ready(function () {
  renderForm(deceasedData,'#deceased-form-container',createDeceasedRow);
});