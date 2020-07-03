import {Mood} from '../../mood/mood';
import {getAverage} from '../functions/average';

export class MoodStatsHelper {

  static getAverageRatingByDay(moods: Mood[]) {
    const monday: number[] = [];
    const tuesday: number[] = [];
    const wednesday: number[] = [];
    const thursday: number[] = [];
    const friday: number[] = [];
    const saturday: number[] = [];
    const sunday: number[] = [];

    for (const mood of moods) {
      const date = new Date(mood.date);
      switch (date.getDay()) {
        case 0:
          sunday.push(mood.rating);
          break;
        case 1:
          monday.push(mood.rating);
          break;
        case 2:
          tuesday.push(mood.rating);
          break;
        case 3:
          wednesday.push(mood.rating);
          break;
        case 4:
          thursday.push(mood.rating);
          break;
        case 5:
          friday.push(mood.rating);
          break;
        case 6:
          saturday.push(mood.rating);
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


}
