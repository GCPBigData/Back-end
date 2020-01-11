import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BrokerageProduct } from 'app/shared/model/brokerage-product.model';
import { BrokerageProductService } from './brokerage-product.service';
import { BrokerageProductComponent } from './brokerage-product.component';
import { BrokerageProductDetailComponent } from './brokerage-product-detail.component';
import { BrokerageProductUpdateComponent } from './brokerage-product-update.component';
import { BrokerageProductDeletePopupComponent } from './brokerage-product-delete-dialog.component';
import { IBrokerageProduct } from 'app/shared/model/brokerage-product.model';

@Injectable({ providedIn: 'root' })
export class BrokerageProductResolve implements Resolve<IBrokerageProduct> {
    constructor(private service: BrokerageProductService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<BrokerageProduct> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<BrokerageProduct>) => response.ok),
                map((brokerageProduct: HttpResponse<BrokerageProduct>) => brokerageProduct.body)
            );
        }
        return of(new BrokerageProduct());
    }
}

export const brokerageProductRoute: Routes = [
    {
        path: 'brokerage-product',
        component: BrokerageProductComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageProduct.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brokerage-product/:id/view',
        component: BrokerageProductDetailComponent,
        resolve: {
            brokerageProduct: BrokerageProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageProduct.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brokerage-product/new',
        component: BrokerageProductUpdateComponent,
        resolve: {
            brokerageProduct: BrokerageProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageProduct.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'brokerage-product/:id/edit',
        component: BrokerageProductUpdateComponent,
        resolve: {
            brokerageProduct: BrokerageProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageProduct.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const brokerageProductPopupRoute: Routes = [
    {
        path: 'brokerage-product/:id/delete',
        component: BrokerageProductDeletePopupComponent,
        resolve: {
            brokerageProduct: BrokerageProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'clivServerApp.brokerageProduct.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
