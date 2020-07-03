import { Solution } from './solution';

export interface Worry {
    date: Date;
    severity: number;
    type: string;
    description: string;
    solutions: Solution[]
}
