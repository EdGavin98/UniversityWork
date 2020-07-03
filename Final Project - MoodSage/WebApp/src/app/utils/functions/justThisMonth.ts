import { Worry } from 'src/app/worry/worry';
import { Mood } from 'src/app/mood/mood';

/**
 * Helper function to filter an array of dates to just the ones from this month.
 * @param dates Array of moods or worries to be filtered, accepts any but requires that object has a .date field (will return false by default if its undefined)
 * @returns Array of filtered dates and worries
 */
export function justThisMonth(dates: any[]): any[] {

    const monthAgo = new Date()
    monthAgo.setMonth(monthAgo.getMonth() - 1)

    return dates.filter((x) => {
        if (x.date != undefined) {
            const date = new Date(x.date)
            return date > monthAgo
        } else {
            return false
        }
    })
}