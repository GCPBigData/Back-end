export interface IMarketSector {
    id?: number;
    name?: string;
}

export class MarketSector implements IMarketSector {
    constructor(public id?: number, public name?: string) {}
}
