import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Brokerage } from 'app/shared/model/brokerage.model';
import { BrokerageService } from './brokerage.service';
import { BrokerageComponent } from './brokerage.component';
import { BrokerageDetailComponent } from './brokerage-detail.component';
import { BrokerageUpdateComponent } from './brokerage-update.component';
import { BrokerageDeletePopupComponent } from './brokerage-delete-dialog.component';
import { IBrokerage } from 'app/shared/model/brokerage.model';

@Injectable({ providedIn: 'root' })
export class BrokerageResolve implements Resolve<IBrokerage> {
    constructor(private service: BrokerageService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Brokerage> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Brokerage>) => response.ok),
                map((brokerage: HttpResponse<Brokerage>) => brokerage.body)
            );
        }
        return of(new Brokerage());
    }
}

export const brokerageRoute: Routes = [
    {
        path: 'brokerage',
        component: BrokerageComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'clivServerApp.brokerage.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brokerage/:id/view',
        component: BrokerageDetailComponent,
        resolve: {
            brokerage: BrokerageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerage.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brokerage/new',
        component: BrokerageUpdateComponent,
        resolve: {
            brokerage: BrokerageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerage.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brokerage/:id/edit',
        component: BrokerageUpdateComponent,
        resolve: {
            brokerage: BrokerageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const brokeragePopupRoute: Routes = [
    {
        path: 'brokerage/:id/delete',
        component: BrokerageDeletePopupComponent,
        resolve: {
            brokerage: BrokerageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
