export interface IStock {
    id?: number;
    symbol?: string;
    company?: string;
    marketSectorName?: string;
    marketSectorId?: number;
}

export class Stock implements IStock {
    constructor(
        public id?: number,
        public symbol?: string,
        public company?: string,
        public marketSectorName?: string,
        public marketSectorId?: number
    ) {}
}
