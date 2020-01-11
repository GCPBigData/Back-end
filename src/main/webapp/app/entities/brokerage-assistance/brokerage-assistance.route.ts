import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BrokerageAssistance } from 'app/shared/model/brokerage-assistance.model';
import { BrokerageAssistanceService } from './brokerage-assistance.service';
import { BrokerageAssistanceComponent } from './brokerage-assistance.component';
import { BrokerageAssistanceDetailComponent } from './brokerage-assistance-detail.component';
import { BrokerageAssistanceUpdateComponent } from './brokerage-assistance-update.component';
import { BrokerageAssistanceDeletePopupComponent } from './brokerage-assistance-delete-dialog.component';
import { IBrokerageAssistance } from 'app/shared/model/brokerage-assistance.model';

@Injectable({ providedIn: 'root' })
export class BrokerageAssistanceResolve implements Resolve<IBrokerageAssistance> {
    constructor(private service: BrokerageAssistanceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<BrokerageAssistance> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<BrokerageAssistance>) => response.ok),
                map((brokerageAssistance: HttpResponse<BrokerageAssistance>) => brokerageAssistance.body)
            );
        }
        return of(new BrokerageAssistance());
    }
}

export const brokerageAssistanceRoute: Routes = [
    {
        path: 'brokerage-assistance',
        component: BrokerageAssistanceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageAssistance.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brokerage-assistance/:id/view',
        component: BrokerageAssistanceDetailComponent,
        resolve: {
            brokerageAssistance: BrokerageAssistanceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageAssistance.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brokerage-assistance/new',
        component: BrokerageAssistanceUpdateComponent,
        resolve: {
            brokerageAssistance: BrokerageAssistanceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageAssistance.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brokerage-assistance/:id/edit',
        component: BrokerageAssistanceUpdateComponent,
        resolve: {
            brokerageAssistance: BrokerageAssistanceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageAssistance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const brokerageAssistancePopupRoute: Routes = [
    {
        path: 'brokerage-assistance/:id/delete',
        component: BrokerageAssistanceDeletePopupComponent,
        resolve: {
            brokerageAssistance: BrokerageAssistanceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageAssistance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
