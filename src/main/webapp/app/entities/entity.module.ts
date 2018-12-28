import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ClivServerMarketSectorModule } from './market-sector/market-sector.module';
import { ClivServerStockModule } from './stock/stock.module';
import { ClivServerStockWatchModule } from './stock-watch/stock-watch.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ClivServerMarketSectorModule,
        ClivServerStockModule,
        ClivServerStockWatchModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClivServerEntityModule {}
