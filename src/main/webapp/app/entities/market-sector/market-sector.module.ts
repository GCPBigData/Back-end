import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClivServerSharedModule } from 'app/shared';
import {
    MarketSectorComponent,
    MarketSectorDetailComponent,
    MarketSectorUpdateComponent,
    MarketSectorDeletePopupComponent,
    MarketSectorDeleteDialogComponent,
    marketSectorRoute,
    marketSectorPopupRoute
} from './';

const ENTITY_STATES = [...marketSectorRoute, ...marketSectorPopupRoute];

@NgModule({
    imports: [ClivServerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MarketSectorComponent,
        MarketSectorDetailComponent,
        MarketSectorUpdateComponent,
        MarketSectorDeleteDialogComponent,
        MarketSectorDeletePopupComponent
    ],
    entryComponents: [
        MarketSectorComponent,
        MarketSectorUpdateComponent,
        MarketSectorDeleteDialogComponent,
        MarketSectorDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClivServerMarketSectorModule {}
