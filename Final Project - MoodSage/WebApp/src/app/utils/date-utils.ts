export class DateUtils {

  private static dateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
  private static timeOptions = { hour: '2-digit', minute: '2-digit'};

  static formatDateWithTime(date: Date): string {
    return `${this.formatDate(date)} at ${date.toLocaleTimeString('en-GB', this.timeOptions)}`;
  }


  static formatDate(date: Date): string {
    return date.toLocaleDateString('en-GB', this.dateOptions);
  }

}
