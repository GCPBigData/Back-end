import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ClivServerMarketSectorModule } from './market-sector/market-sector.module';
import { ClivServerStockModule } from './stock/stock.module';
import { ClivServerStockWatchModule } from './stock-watch/stock-watch.module';
import { ClivServerBrokerageClientModule } from './brokerage-client/brokerage-client.module';
import { ClivServerBrokerageModule } from './brokerage/brokerage.module';
import { ClivServerBrokerageProductModule } from './brokerage-product/brokerage-product.module';
import { ClivServerBrokerageAssistanceModule } from './brokerage-assistance/brokerage-assistance.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ClivServerMarketSectorModule,
        ClivServerStockModule,
        ClivServerStockWatchModule,
        ClivServerBrokerageClientModule,
        ClivServerBrokerageModule,
        ClivServerBrokerageProductModule,
        ClivServerBrokerageAssistanceModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClivServerEntityModule {}
