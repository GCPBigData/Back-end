import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClivServerSharedModule } from 'app/shared';
import {
    BrokerageAssistanceComponent,
    BrokerageAssistanceDetailComponent,
    BrokerageAssistanceUpdateComponent,
    BrokerageAssistanceDeletePopupComponent,
    BrokerageAssistanceDeleteDialogComponent,
    brokerageAssistanceRoute,
    brokerageAssistancePopupRoute
} from './';

const ENTITY_STATES = [...brokerageAssistanceRoute, ...brokerageAssistancePopupRoute];

@NgModule({
    imports: [ClivServerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BrokerageAssistanceComponent,
        BrokerageAssistanceDetailComponent,
        BrokerageAssistanceUpdateComponent,
        BrokerageAssistanceDeleteDialogComponent,
        BrokerageAssistanceDeletePopupComponent
    ],
    entryComponents: [
        BrokerageAssistanceComponent,
        BrokerageAssistanceUpdateComponent,
        BrokerageAssistanceDeleteDialogComponent,
        BrokerageAssistanceDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ClivServerBrokerageAssistanceModule {}
