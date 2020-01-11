import { IBrokerage } from 'app/shared/model//brokerage.model';

export interface IBrokerageProduct {
    id?: number;
    name?: string;
    brokerages?: IBrokerage[];
}

export class BrokerageProduct implements IBrokerageProduct {
    constructor(public id?: number, public name?: string, public brokerages?: IBrokerage[]) {}
}
