import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Mood} from '../mood';
import {DateUtils} from '../../utils/date-utils';
import { trigger, state, transition, style, animate } from '@angular/animations';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-mood-list',
  templateUrl: './mood-list.component.html',
  styleUrls: ['./mood-list.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0', display: 'none'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ]
})
export class MoodListComponent implements OnInit {

  constructor() { }

  @Input() moods: Mood[];
  tableData: MatTableDataSource<Mood>;
  displayedColumns = ['date', 'rating'];
  expandedMood: Mood | null;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;



  ngOnInit(): void {
    this.tableData = new MatTableDataSource<Mood>(this.moods);
    this.tableData.paginator = this.paginator
    this.tableData.sort = this.sort
  }

  getDate(date: Date) {
    const dateObject = new Date(date);
    return DateUtils.formatDate(dateObject);
  }

}
