import { Mood } from '../mood/mood';
import { Worry } from '../worry/worry';
import { justThisMonth } from '../utils/functions/justThisMonth';

export class Patient {
    forename: string;
    surname: string;
    email: string;
    moods: Mood[];
    worries: Worry[];
    moodTarget: number;
    worryTarget: number;

    constructor(json: any) {
      this.forename  = json.forename;
      this.surname = json.surname;
      this.email = json.email;
      this.moods = json.moods;
      this.worries = json.worries;
      this.moodTarget = json.moodTarget;
      this.worryTarget = json.worryTarget;
    }

    private getAverage(array: number[]): number {
      return array.reduce((a , b) => a + b) / array.length;
    }

    getWorryAverage(): number {
      if (this.worries.length > 0) {
        const ratings: number[] = [];
        this.worries.map(worry => ratings.push(worry.severity));
        return this.getAverage(ratings);
      } else {
        return 0;
      }
    }

    getMoodAverage(): number {
      if (this.moods.length > 0) {
        const ratings: number[] = [];
        this.moods.map(mood => ratings.push(mood.rating));
        return this.getAverage(ratings);
      } else {
        return 0;
      }
    }

    getWorryAverageThisMonth(): number {
      const worriesThisMonth = justThisMonth(this.worries)
      if (worriesThisMonth.length > 0) {
        const ratings: number[] = [];
        worriesThisMonth.map(worry => ratings.push(worry.severity));
        return this.getAverage(ratings);
      } else {
        return 0;
      }
    }

    getMoodAverageThisMonth(): number {
      const moodsThisMonth = justThisMonth(this.moods)

      if (moodsThisMonth.length > 0) {
        const ratings: number[] = [];
        moodsThisMonth.map(mood => ratings.push(mood.rating));
        return this.getAverage(ratings);
      } else {
        return 0;
      }
    }
}
