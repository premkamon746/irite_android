var deceasedData = [];
    if (isMobileDevice()) {
      try {
        deceasedData = JSON.parse(jsonObject.deceased_name);
      } catch (e) {
         deceasedData = []
      } 
    }

    function createDeceasedRow(data = {}) {
      const $row = $(`
        <div class="row pr-3 pl-3 align-items-end mb-3 deceased-row">
          <div class="col-9 col-sm-10 col-md-6 col-lg-4 col-xl-4 mb-3">
            <label class="form-label" style="font-size: 16px">ชื่อผู้เสียชีวิต</label>
            <input type="text" class="form-control form-control-sm deceased-name" placeholder="ระบุชื่อ - นามสกุล ผู้เสียชีวิต/บาดเจ็บ">
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
        checkAndAddNewRow('.deceased-row', '#deceased-form-container',createDeceasedRow);
      });

      // ปุ่มลบ
      $row.find('.btn-remove').on('click', function () {
        const $rows = $('.deceased-row');
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
          saveListData($row.index(), updatedData, deceasedData , "deceased_name" );
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

    function createVictimRow(data = {}) {
      const $row = $(`
        <div class="row align-items-end mb-3 victim-row">
          <div class="col-9 col-sm-10 col-md-6 col-lg-4 col-xl-4">
            <label class="form-label" style="font-size: 16px">ชื่อผู้เสียหาย</label>
            <input type="text" class="form-control form-control-sm victim-name dynamic" name="victim-name[]" placeholder="ระบุชื่อ - นามสกุล ผู้เสียหาย">
          </div>
          <div class="col-3 col-sm-2 col-md-2 col-lg-2 col-xl-2">
            <label class="form-label" style="font-size: 16px">อายุ (ปี)</label>
            <input type="number" class="form-control form-control-sm victim-age dynamic" name="victim-age[]" min="0" placeholder="ระบุอายุ">
          </div>
          <div class="col-12 col-sm-2 col-md-2 col-lg-2 col-xl-2 text-end">
            <button type="button" class="btn btn-danger btn-sm btn-remove">ลบ</button>
          </div>
        </div>
      `);

      // กรอกข้อมูลจาก JSON ถ้ามี
      $row.find('.victim-name').val(data.name || '');
      $row.find('.victim-age').val(data.age || '');
      $row.find('.victim-phone').val(data.phone || '');

      // ตรวจสอบการเปลี่ยน input แล้วเพิ่มแถว
      $row.find('input').on('input', function () {
        checkAndAddNewRow('.victim-row', '#victim-form-container',createVictimRow);
      });

      // ลบแถว
      $row.find('.btn-remove').on('click', function () {
        const $rows = $('.victim-row');
        if ($rows.length > 1) {
          $row.remove();
          const index = $row.index();
          victimData.splice(index, 1);
          sharedHandler()
        } else {
          // ถ้าเหลือแถวเดียว ให้ล้างค่าแทน
          $row.find('input').val('');
        }
      });

      // จำกัดให้กรอกเฉพาะตัวเลขในเบอร์โทร
      $row.find('.victim-phone').on('input', function () {
        this.value = this.value.replace(/[^0-9]/g, '').slice(0, 10);
      });

      let saveTimeout;
      $row.find('input').on('input blur',sharedHandler)
      function sharedHandler(event){
        clearTimeout(saveTimeout);
        saveTimeout = setTimeout(() => {
          const updatedData = {
            name: $row.find('.victim-name').val(),
            age: $row.find('.victim-age').val(),
            phone: $row.find('.victim-phone').val(),
          };
          //console.log('Auto-saving victim data:', updatedData);
          saveListData($row.index(), updatedData, victimData , "victim_name" );
        }, 500); // Save after 300ms pause
      } 
      return $row;
    }

    var injuredData = []
    if (isMobileDevice()) {
      try {
        injuredData = JSON.parse(jsonObject.injured_name);
      } catch (e) {
         injuredData = []
      } 
    }

    function createInjuredRow(data = {}) {
      const $row = $(`
        <div class="row align-items-end mb-3 injured-row">
          <div class="col-9 col-sm-10 col-md-6 col-lg-4 col-xl-4">
            <label class="form-label" style="font-size: 16px">บาดเจ็บ</label>
            <input type="text" class="form-control form-control-sm injured-name dynamic" name="injured-name[]" placeholder="ระบุชื่อ - นามสกุล ผู้เสียหาย">
          </div>
          <div class="col-3 col-sm-2 col-md-2 col-lg-2 col-xl-2">
            <label class="form-label" style="font-size: 16px">อายุ (ปี)</label>
            <input type="number" class="form-control form-control-sm injured-age dynamic" name="injured-age[]" min="0" placeholder="ระบุอายุ">
          </div>
          <div class="col-12 col-sm-2 col-md-2 col-lg-2 col-xl-2 text-end">
            <button type="button" class="btn btn-danger btn-sm btn-remove">ลบ</button>
          </div>
        </div>
      `);

      // กรอกข้อมูลจาก JSON ถ้ามี
      $row.find('.injured-name').val(data.name || '');
      $row.find('.injured-age').val(data.age || '');
      $row.find('.injured-phone').val(data.phone || '');

      // ตรวจสอบการเปลี่ยน input แล้วเพิ่มแถว
      $row.find('input').on('input', function () {
        checkAndAddNewRow('.injured-row', '#injured-form-container',createInjuredRow);
      });

      // ลบแถว
      $row.find('.btn-remove').on('click', function () {
        const $rows = $('.injured-row');
        if ($rows.length > 1) {
          $row.remove();
          const index = $row.index();
          injuredData.splice(index, 1);
          sharedHandler()
        } else {
          // ถ้าเหลือแถวเดียว ให้ล้างค่าแทน
          $row.find('input').val('');
        }
      });

      // จำกัดให้กรอกเฉพาะตัวเลขในเบอร์โทร
      $row.find('.injured-phone').on('input', function () {
        this.value = this.value.replace(/[^0-9]/g, '').slice(0, 10);
      });

      let saveTimeout;
      $row.find('input').on('input blur',sharedHandler)
      function sharedHandler(event){
        clearTimeout(saveTimeout);
        saveTimeout = setTimeout(() => {
          const updatedData = {
            name: $row.find('.injured-name').val(),
            age: $row.find('.injured-age').val(),
            phone: $row.find('.injured-phone').val(),
          };
          //console.log('Auto-saving injured data:', updatedData);
          saveListData($row.index(), updatedData, injuredData , "injured_name" );
        }, 500); // Save after 300ms pause
      } 
      return $row;
    }



// เริ่มโหลด
$(document).ready(function () {
  renderForm(victimData,'#victim-form-container',createVictimRow);
  renderForm(deceasedData,'#deceased-form-container',createDeceasedRow);
  renderForm(injuredData,'#injured-form-container',createInjuredRow);
});