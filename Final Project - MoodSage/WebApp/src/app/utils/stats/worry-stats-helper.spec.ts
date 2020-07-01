import { WorryStatsHelper } from './worry-stats-helper';
import { Worry } from 'src/app/worry/worry';
import { getAverage } from '../functions/average';

describe('WorryStatsHelper', () => {
  
  it("should return current/hypothetical count 0 each on empty array", () => {
    let counts = WorryStatsHelper.getHypotheticalAndCurrentCount([]);

    expect(counts[0]).toBe(0);
    expect(counts[1]).toBe(0);
  });
  it("should return the correct current/hypothetical split", () => {
    let worryArray = [
      {date: new Date(), description: "", type: "Current", severity: 1, solutions: []},
      {date: new Date(), description: "", type: "Current", severity: 1, solutions: []},
      {date: new Date(), description: "", type: "Hypothetical", severity: 1, solutions: []},
      {date: new Date(), description: "", type: "Current", severity: 1, solutions: []},
      {date: new Date(), description: "", type: "Hypothetical", severity: 1, solutions: []},
    ]
    let counts = WorryStatsHelper.getHypotheticalAndCurrentCount(worryArray);

    expect(counts[0]).toBe(3);
    expect(counts[1]).toBe(2);
  });
  it("should return all zeros for daily average on empty array", () => {
    let averages = WorryStatsHelper.getAverageSeverityByDay([]);

    expect(averages).toEqual([0,0,0,0,0,0,0])
  });
  it("should return the correct averages for each day", () => {
    let worryArray = [
      {date: new Date(2020, 4, 1), description: "", type: "Current", severity: 1, solutions: []},
      {date: new Date(2020, 4, 1), description: "", type: "Current", severity: 3, solutions: []},
      {date: new Date(2020, 4, 2), description: "", type: "Hypothetical", severity: 2, solutions: []},
      {date: new Date(2020, 4, 2), description: "", type: "Current", severity: 4, solutions: []},
      {date: new Date(2020, 4, 2), description: "", type: "Hypothetical", severity: 6, solutions: []},
      {date: new Date(2020, 4, 2), description: "", type: "Hypothetical", severity: 8, solutions: []},
      {date: new Date(2020, 4, 3), description: "", type: "Hypothetical", severity: 2, solutions: []},
      {date: new Date(2020, 4, 4), description: "", type: "Hypothetical", severity: 2, solutions: []},
      {date: new Date(2020, 4, 5), description: "", type: "Hypothetical", severity: 4, solutions: []},
      {date: new Date(2020, 4, 5), description: "", type: "Hypothetical", severity: 5, solutions: []},
    ]

    
    let averages = WorryStatsHelper.getAverageSeverityByDay(worryArray);
    
    expect(averages).toEqual([2,4.5,0,0,2,5,2])
  })
  it("should return zero worries with solution on empty array", () => {
    let numSolutions = WorryStatsHelper.getCountWithSolutions([])
    expect(numSolutions).toBe(0);
  });
  it("should return correct number of worries with solutions", () => {
    let worryArray = [
      {date: new Date(2020, 4, 1), description: "", type: "Current", severity: 1, solutions: [{description: "", advantages: "", disadvantages: "", timeLogged: new Date()}, {description: "", advantages: "", disadvantages: "", timeLogged: new Date()}]},
      {date: new Date(2020, 4, 1), description: "", type: "Current", severity: 3, solutions: []},
      {date: new Date(2020, 4, 2), description: "", type: "Hypothetical", severity: 2, solutions: [{description: "", advantages: "", disadvantages: "", timeLogged: new Date()}]},
      {date: new Date(2020, 4, 2), description: "", type: "Current", severity: 4, solutions: []}
    ]

    let numSolutions = WorryStatsHelper.getCountWithSolutions(worryArray)
    expect(numSolutions).toBe(2);
  });
  it("should return nothing (no worries per day) on empty array", () => {
      let numPerDay = WorryStatsHelper.getNumWorriesPerDay([]);
      expect(numPerDay).toBeUndefined();
  });
  it("should return correct number of worries per day", () => {
    let worryArray = [
      {date: new Date(2020, 4, 1), description: "", type: "Current", severity: 1, solutions: []},
      {date: new Date(2020, 4, 1), description: "", type: "Current", severity: 3, solutions: []},
      {date: new Date(2020, 4, 2), description: "", type: "Hypothetical", severity: 2, solutions: []},
      {date: new Date(2020, 4, 2), description: "", type: "Current", severity: 4, solutions: []},
      {date: new Date(2020, 4, 2), description: "", type: "Hypothetical", severity: 6, solutions: []},
      {date: new Date(2020, 4, 2), description: "", type: "Hypothetical", severity: 8, solutions: []},
      {date: new Date(2020, 4, 3), description: "", type: "Hypothetical", severity: 2, solutions: []},
      {date: new Date(2020, 4, 4), description: "", type: "Hypothetical", severity: 2, solutions: []},
      {date: new Date(2020, 4, 5), description: "", type: "Hypothetical", severity: 4, solutions: []},
      {date: new Date(2020, 4, 5), description: "", type: "Hypothetical", severity: 5, solutions: []},
    ]
    let numPerDay = WorryStatsHelper.getNumWorriesPerDay(worryArray);
    expect(numPerDay[0].y).toBe(2);
    expect(numPerDay[1].y).toBe(4);
    expect(numPerDay[2].y).toBe(1);
    expect(numPerDay[3].y).toBe(1);
    expect(numPerDay[4].y).toBe(2);
  });


});
