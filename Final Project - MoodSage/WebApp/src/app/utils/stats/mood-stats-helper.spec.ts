import { MoodStatsHelper } from './mood-stats-helper';

describe('MoodStatsHelper', () => {
  it('should create an instance', () => {
    expect(new MoodStatsHelper()).toBeTruthy();
  });
  it("should return array of 0 for no moods", () => {
    let dailyAverage = MoodStatsHelper.getAverageRatingByDay([]);
    expect(dailyAverage).toEqual([0,0,0,0,0,0,0]);
  });
  it("should return correct averages", () => {
    let moodArray = [
      {date: new Date(2020, 4, 1), comment: "", rating: 3}, 
      {date: new Date(2020, 4, 1), comment: "", rating: 2}, 
      {date: new Date(2020, 4, 1), comment: "", rating: 4}, 
      {date: new Date(2020, 4, 3), comment: "", rating: 1},
      {date: new Date(2020, 4, 4), comment: "", rating: 5},
      {date: new Date(2020, 4, 4), comment: "", rating: 6},
      {date: new Date(2020, 4, 6), comment: "", rating: 3},
      {date: new Date(2020, 4, 6), comment: "", rating: 2}
    ]

    let dailyAverage = MoodStatsHelper.getAverageRatingByDay(moodArray);
    expect(dailyAverage).toEqual([5.5,0,2.5,0,3,0,1]);
  });
});
