/**
 * App Calendar Events
 */

'use strict';

let date = new Date();
let nextDay = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
// prettier-ignore
let nextMonth = date.getMonth() === 11 ? new Date(date.getFullYear() + 1, 0, 1) : new Date(date.getFullYear(), date.getMonth() + 1, 1);
// prettier-ignore
let prevMonth = date.getMonth() === 11 ? new Date(date.getFullYear() - 1, 0, 1) : new Date(date.getFullYear(), date.getMonth() - 1, 1);

let events = [
  {
    id: 1,
    url: '',
    title: 'เตรียมความพร้อม ชุดที่ 1',
    start: date,
    end: nextDay,
    allDay: false,
    extendedProps: {
      calendar: 'checklist'
    }
  },
  {
    id: 2,
    url: '',
    title: 'เตรียมความพร้อม ชุดที่ 2',
    start: date,
    end: nextDay,
    allDay: false,
    extendedProps: {
      calendar: 'checklist'
    }
  },
  {
    id: 3,
    url: '',
    title: 'เพลงไหม้',
    start: date,
    end: nextDay,
    allDay: true,
    extendedProps: {
      calendar: 'fired'
    }
  },
  {
    id: 3,
    url: '',
    title: 'ชีวิต',
    start: date,
    end: nextDay,
    allDay: true,
    extendedProps: {
      calendar: 'life'
    }
  }
  ]
