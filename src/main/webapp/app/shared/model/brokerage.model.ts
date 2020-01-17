import { IBrokerageClient } from 'app/shared/model//brokerage-client.model';
import { IBrokerageProduct } from 'app/shared/model//brokerage-product.model';
import { IBrokerageAssistance } from 'app/shared/model//brokerage-assistance.model';

export interface IBrokerage {
    id?: number;
    name?: string;
    cnpj?: string;
    address?: string;
    addressNeighborhood?: string;
    addressCity?: string;
    addressState?: string;
    swingTrade?: boolean;
    dayTrade?: boolean;
    loginEmail?: boolean;
    loginAccessCode?: boolean;
    loginCpf?: boolean;
    loginPassword?: boolean;
    loginToken?: boolean;
    fee?: number;
    iss?: number;
    phone?: string;
    website?: string;
    email?: string;
    logo?: string;
    brokerageClients?: IBrokerageClient[];
    brokerageProducts?: IBrokerageProduct[];
    brokerageAssistances?: IBrokerageAssistance[];
}

export class Brokerage implements IBrokerage {
    constructor(
        public id?: number,
        public name?: string,
        public cnpj?: string,
        public address?: string,
        public addressNeighborhood?: string,
        public addressCity?: string,
        public addressState?: string,
        public swingTrade?: boolean,
        public dayTrade?: boolean,
        public loginEmail?: boolean,
        public loginAccessCode?: boolean,
        public loginCpf?: boolean,
        public loginPassword?: boolean,
        public loginToken?: boolean,
        public fee?: number,
        public iss?: number,
        public phone?: string,
        public website?: string,
        public email?: string,
        public logo?: string,
        public brokerageClients?: IBrokerageClient[],
        public brokerageProducts?: IBrokerageProduct[],
        public brokerageAssistances?: IBrokerageAssistance[]
    ) {
        this.swingTrade = this.swingTrade || false;
        this.dayTrade = this.dayTrade || false;
        this.loginEmail = this.loginEmail || false;
        this.loginAccessCode = this.loginAccessCode || false;
        this.loginCpf = this.loginCpf || false;
        this.loginPassword = this.loginPassword || false;
        this.loginToken = this.loginToken || false;
    }
}
