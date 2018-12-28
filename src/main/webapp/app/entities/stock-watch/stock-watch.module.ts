import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClivServerSharedModule } from 'app/shared';
import { ClivServerAdminModule } from 'app/admin/admin.module';
import {
    StockWatchComponent,
    StockWatchDetailComponent,
    StockWatchUpdateComponent,
    StockWatchDeletePopupComponent,
    StockWatchDeleteDialogComponent,
    stockWatchRoute,
    stockWatchPopupRoute
} from './';

const ENTITY_STATES = [...stockWatchRoute, ...stockWatchPopupRoute];

@NgModule({
    imports: [ClivServerSharedModule, ClivServerAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        StockWatchComponent,
        StockWatchDetailComponent,
        StockWatchUpdateComponent,
        StockWatchDeleteDialogComponent,
        StockWatchDeletePopupComponent
    ],
    entryComponents: [StockWatchComponent, StockWatchUpdateComponent, StockWatchDeleteDialogComponent, StockWatchDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClivServerStockWatchModule {}
