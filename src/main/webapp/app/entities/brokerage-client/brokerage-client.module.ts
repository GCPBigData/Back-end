import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClivServerSharedModule } from 'app/shared';
import {
    BrokerageClientComponent,
    BrokerageClientDetailComponent,
    BrokerageClientUpdateComponent,
    BrokerageClientDeletePopupComponent,
    BrokerageClientDeleteDialogComponent,
    brokerageClientRoute,
    brokerageClientPopupRoute
} from './';

const ENTITY_STATES = [...brokerageClientRoute, ...brokerageClientPopupRoute];

@NgModule({
    imports: [ClivServerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BrokerageClientComponent,
        BrokerageClientDetailComponent,
        BrokerageClientUpdateComponent,
        BrokerageClientDeleteDialogComponent,
        BrokerageClientDeletePopupComponent
    ],
    entryComponents: [
        BrokerageClientComponent,
        BrokerageClientUpdateComponent,
        BrokerageClientDeleteDialogComponent,
        BrokerageClientDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClivServerBrokerageClientModule {}
