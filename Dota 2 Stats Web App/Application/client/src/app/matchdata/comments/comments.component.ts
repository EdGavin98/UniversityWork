import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import * as io from 'socket.io-client';
import { MatchService } from 'src/app/match.service';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {

  constructor(private service : MatchService) { }
  @Input() matchId;
  comments : any;
  socket : any;
  error = false;


  commentUserName : String =  "";
  commentBody : String = "";

  ngOnInit() {
    this.getComments();
    this.socket = io("localhost:8080/comment-namespace");
    this.socket.emit("join", this.matchId);


    this.socket.on("new-comment", () => {
      this.getComments();
    })
  }

  ngOnDestroy() {
    this.socket.emit("leave", this.matchId);
    this.socket.disconnect();
  }

  postComment() {
    if (this.commentUserName.trim() != "" && this.commentBody.trim() != "") {
      this.socket.emit("comment", {
        matchId : this.matchId,
        name : this.commentUserName,
        body : this.commentBody
      });
      this.error = false;
      this.commentUserName = "";
      this.commentBody = "";
    } else {
      this.error = true;
    }
    
  }

  getComments() {
    this.service.getMatchComments(this.matchId).subscribe((obj : Response) => {
      this.comments = obj;
    });
  }

  shouldShowComments() : boolean {
    return !(this.comments == undefined || this.comments.length == 0);
  }

  formattedDate(date) {
    var tempDate = new Date(date);
    var mins = tempDate.getMinutes() < 10 ? "0" + tempDate.getMinutes() : tempDate.getMinutes();
    return tempDate.getUTCDate() + "/" + (tempDate.getUTCMonth() + 1) + "/" + tempDate.getFullYear()
     + " at " + tempDate.getHours() + ":" + mins;
  }

}
