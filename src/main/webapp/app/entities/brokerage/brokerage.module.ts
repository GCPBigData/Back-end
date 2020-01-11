import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClivServerSharedModule } from 'app/shared';
import {
    BrokerageComponent,
    BrokerageDetailComponent,
    BrokerageUpdateComponent,
    BrokerageDeletePopupComponent,
    BrokerageDeleteDialogComponent,
    brokerageRoute,
    brokeragePopupRoute
} from './';

const ENTITY_STATES = [...brokerageRoute, ...brokeragePopupRoute];

@NgModule({
    imports: [ClivServerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BrokerageComponent,
        BrokerageDetailComponent,
        BrokerageUpdateComponent,
        BrokerageDeleteDialogComponent,
        BrokerageDeletePopupComponent
    ],
    entryComponents: [BrokerageComponent, BrokerageUpdateComponent, BrokerageDeleteDialogComponent, BrokerageDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClivServerBrokerageModule {}
