import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Worry} from '../worry';
import { DateUtils } from '../../utils/date-utils';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-worry-list',
  templateUrl: './worry-list.component.html',
  styleUrls: ['./worry-list.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0', display: 'none'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ]
})
export class WorryListComponent implements OnInit {

  @Input() worries: Worry[];
  tableData: MatTableDataSource<Worry>
  displayedColumns: string[] = ['date', 'type', 'severity'];
  expandedWorry: Worry | null
  
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;



  constructor() { }

  ngOnInit(): void {
    this.tableData = new MatTableDataSource<Worry>(this.worries);
    this.tableData.paginator = this.paginator
    this.tableData.sort = this.sort

  }

  getDate(date: Date) {
    const dateObject = new Date(date);
    return DateUtils.formatDateWithTime(dateObject);
  }
  
}
