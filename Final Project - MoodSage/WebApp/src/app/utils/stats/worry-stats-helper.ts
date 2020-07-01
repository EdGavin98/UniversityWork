import {Worry} from '../../worry/worry';
import {getAverage} from '../functions/average';

export class WorryStatsHelper {

  static getHypotheticalAndCurrentCount(worries: Worry[]): number[] {
    let numCurrent = 0;
    let numHypothetical = 0;

    for (const worry of worries) {
      const type = worry.type;
      if (type === 'Current') {
        numCurrent++;
      } else {
        numHypothetical++;
      }
    }

    return [numCurrent, numHypothetical];
  }

  static getAverageSeverityByDay(worries: Worry[]): number[] {
    const monday: number[] = [];
    const tuesday: number[] = [];
    const wednesday: number[] = [];
    const thursday: number[] = [];
    const friday: number[] = [];
    const saturday: number[] = [];
    const sunday: number[] = [];

    for (const worry of worries) {
      const date = new Date(worry.date);
      switch (date.getDay()) {
        case 0:
          sunday.push(worry.severity);
          break;
        case 1:
          monday.push(worry.severity);
          break;
        case 2:
          tuesday.push(worry.severity);
          break;
        case 3:
          wednesday.push(worry.severity);
          break;
        case 4:
          thursday.push(worry.severity);
          break;
        case 5:
          friday.push(worry.severity);
          break;
        case 6:
          saturday.push(worry.severity);
          break;
        default:
          break;
      }
    }

    return [
      getAverage(monday),
      getAverage(tuesday),
      getAverage(wednesday),
      getAverage(thursday),
      getAverage(friday),
      getAverage(saturday),
      getAverage(sunday),
    ];
  }

  static getCountWithSolutions(worries: Worry[]): number {
    let count = 0;
    
    for (const worry of worries) {
      if (worry.solutions.length > 0) {
        count++;
      }
    }

    return count;
  }

  static getNumWorriesPerDay(worries: Worry[]): any {
    if (worries.length > 0) {
      let total = 0;
      let data = [{
        x: new Date(worries[0].date),
        y: 0
      }];
      data[0].x.setUTCHours(0);
      data[0].x.setUTCMinutes(0);

      for (const worry of worries) {
        let date = new Date(worry.date)
        date.setUTCHours(0);
        date.setUTCMinutes(0);
        if (date.getUTCDate() === data[total].x.getUTCDate() && date.getUTCMonth() === data[total].x.getUTCMonth()) {
            data[total].y++
        } else {
          data.push({
            x: date,
            y: 1
          })
           total++
        }
      }

      return data

    } else {
      return
    }
    
  } 

}
