import { IBrokerage } from 'app/shared/model//brokerage.model';

export interface IBrokerageClient {
    id?: number;
    name?: string;
    brokerages?: IBrokerage[];
}

export class BrokerageClient implements IBrokerageClient {
    constructor(public id?: number, public name?: string, public brokerages?: IBrokerage[]) {}
}
