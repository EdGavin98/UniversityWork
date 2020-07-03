export function getAverage(numbers: number[]): number {
    if (numbers.length > 0) {
      return numbers.reduce((a, b) => a + b) / numbers.length;
    } else {
      return 0;
    }
}
