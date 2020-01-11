import { IBrokerage } from 'app/shared/model//brokerage.model';

export interface IBrokerageAssistance {
    id?: number;
    name?: string;
    brokerages?: IBrokerage[];
}

export class BrokerageAssistance implements IBrokerageAssistance {
    constructor(public id?: number, public name?: string, public brokerages?: IBrokerage[]) {}
}
