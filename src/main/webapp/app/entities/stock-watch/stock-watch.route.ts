import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StockWatch } from 'app/shared/model/stock-watch.model';
import { StockWatchService } from './stock-watch.service';
import { StockWatchComponent } from './stock-watch.component';
import { StockWatchDetailComponent } from './stock-watch-detail.component';
import { StockWatchUpdateComponent } from './stock-watch-update.component';
import { StockWatchDeletePopupComponent } from './stock-watch-delete-dialog.component';
import { IStockWatch } from 'app/shared/model/stock-watch.model';

@Injectable({ providedIn: 'root' })
export class StockWatchResolve implements Resolve<IStockWatch> {
    constructor(private service: StockWatchService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<StockWatch> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<StockWatch>) => response.ok),
                map((stockWatch: HttpResponse<StockWatch>) => stockWatch.body)
            );
        }
        return of(new StockWatch());
    }
}

export const stockWatchRoute: Routes = [
    {
        path: 'stock-watch',
        component: StockWatchComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'clivServerApp.stockWatch.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'stock-watch/:id/view',
        component: StockWatchDetailComponent,
        resolve: {
            stockWatch: StockWatchResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.stockWatch.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'stock-watch/new',
        component: StockWatchUpdateComponent,
        resolve: {
            stockWatch: StockWatchResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.stockWatch.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'stock-watch/:id/edit',
        component: StockWatchUpdateComponent,
        resolve: {
            stockWatch: StockWatchResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.stockWatch.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const stockWatchPopupRoute: Routes = [
    {
        path: 'stock-watch/:id/delete',
        component: StockWatchDeletePopupComponent,
        resolve: {
            stockWatch: StockWatchResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.stockWatch.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
