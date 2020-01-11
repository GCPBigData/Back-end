import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClivServerSharedModule } from 'app/shared';
import {
    BrokerageProductComponent,
    BrokerageProductDetailComponent,
    BrokerageProductUpdateComponent,
    BrokerageProductDeletePopupComponent,
    BrokerageProductDeleteDialogComponent,
    brokerageProductRoute,
    brokerageProductPopupRoute
} from './';

const ENTITY_STATES = [...brokerageProductRoute, ...brokerageProductPopupRoute];

@NgModule({
    imports: [ClivServerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BrokerageProductComponent,
        BrokerageProductDetailComponent,
        BrokerageProductUpdateComponent,
        BrokerageProductDeleteDialogComponent,
        BrokerageProductDeletePopupComponent
    ],
    entryComponents: [
        BrokerageProductComponent,
        BrokerageProductUpdateComponent,
        BrokerageProductDeleteDialogComponent,
        BrokerageProductDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClivServerBrokerageProductModule {}
