export interface IStockWatch {
    id?: number;
    stockCompany?: string;
    stockId?: number;
    userLogin?: string;
    userId?: number;
}

export class StockWatch implements IStockWatch {
    constructor(
        public id?: number,
        public stockCompany?: string,
        public stockId?: number,
        public userLogin?: string,
        public userId?: number
    ) {}
}
