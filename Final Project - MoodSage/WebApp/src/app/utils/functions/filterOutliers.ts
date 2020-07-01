export function filterOutliers(values: any[]): {}[] {  

    let maxDist = 4


    for (let i = 1; i < values.length - 1; i++) {
        
    }

    let filtered = values.filter((x: any, idx: number, all: any[]) => {
        if (idx === 0 || idx === all.length - 1) {
            return true
        }

        let left = all[idx - 1].y
        let right = all[idx + 1].y

        return (Math.abs(x.y - left) < maxDist) && (Math.abs(x.y - right) < maxDist)

    })

    return filtered
}