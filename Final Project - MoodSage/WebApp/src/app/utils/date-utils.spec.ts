import { DateUtils } from './date-utils';

describe('DateUtils', () => {
  it('should create an instance', () => {
    expect(new DateUtils()).toBeTruthy();
  });
  it("should format date with time correctly", () => {
    let formattedDate = DateUtils.formatDateWithTime(new Date(2020, 4, 1));
    expect(formattedDate).toBe("Friday, 1 May 2020 at 00:00");
  });
  it("should format date correctly", () => {
    let formattedDate = DateUtils.formatDate(new Date(2020, 4, 1));
    expect(formattedDate).toBe("Friday, 1 May 2020");
  });
});
