import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MarketSector } from 'app/shared/model/market-sector.model';
import { MarketSectorService } from './market-sector.service';
import { MarketSectorComponent } from './market-sector.component';
import { MarketSectorDetailComponent } from './market-sector-detail.component';
import { MarketSectorUpdateComponent } from './market-sector-update.component';
import { MarketSectorDeletePopupComponent } from './market-sector-delete-dialog.component';
import { IMarketSector } from 'app/shared/model/market-sector.model';

@Injectable({ providedIn: 'root' })
export class MarketSectorResolve implements Resolve<IMarketSector> {
    constructor(private service: MarketSectorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<MarketSector> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<MarketSector>) => response.ok),
                map((marketSector: HttpResponse<MarketSector>) => marketSector.body)
            );
        }
        return of(new MarketSector());
    }
}

export const marketSectorRoute: Routes = [
    {
        path: 'market-sector',
        component: MarketSectorComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'clivServerApp.marketSector.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'market-sector/:id/view',
        component: MarketSectorDetailComponent,
        resolve: {
            marketSector: MarketSectorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.marketSector.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'market-sector/new',
        component: MarketSectorUpdateComponent,
        resolve: {
            marketSector: MarketSectorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.marketSector.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'market-sector/:id/edit',
        component: MarketSectorUpdateComponent,
        resolve: {
            marketSector: MarketSectorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.marketSector.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const marketSectorPopupRoute: Routes = [
    {
        path: 'market-sector/:id/delete',
        component: MarketSectorDeletePopupComponent,
        resolve: {
            marketSector: MarketSectorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.marketSector.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
