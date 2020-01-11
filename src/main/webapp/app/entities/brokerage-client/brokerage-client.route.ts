import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BrokerageClient } from 'app/shared/model/brokerage-client.model';
import { BrokerageClientService } from './brokerage-client.service';
import { BrokerageClientComponent } from './brokerage-client.component';
import { BrokerageClientDetailComponent } from './brokerage-client-detail.component';
import { BrokerageClientUpdateComponent } from './brokerage-client-update.component';
import { BrokerageClientDeletePopupComponent } from './brokerage-client-delete-dialog.component';
import { IBrokerageClient } from 'app/shared/model/brokerage-client.model';

@Injectable({ providedIn: 'root' })
export class BrokerageClientResolve implements Resolve<IBrokerageClient> {
    constructor(private service: BrokerageClientService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<BrokerageClient> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<BrokerageClient>) => response.ok),
                map((brokerageClient: HttpResponse<BrokerageClient>) => brokerageClient.body)
            );
        }
        return of(new BrokerageClient());
    }
}

export const brokerageClientRoute: Routes = [
    {
        path: 'brokerage-client',
        component: BrokerageClientComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageClient.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brokerage-client/:id/view',
        component: BrokerageClientDetailComponent,
        resolve: {
            brokerageClient: BrokerageClientResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageClient.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brokerage-client/new',
        component: BrokerageClientUpdateComponent,
        resolve: {
            brokerageClient: BrokerageClientResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageClient.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brokerage-client/:id/edit',
        component: BrokerageClientUpdateComponent,
        resolve: {
            brokerageClient: BrokerageClientResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageClient.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const brokerageClientPopupRoute: Routes = [
    {
        path: 'brokerage-client/:id/delete',
        component: BrokerageClientDeletePopupComponent,
        resolve: {
            brokerageClient: BrokerageClientResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageClient.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
