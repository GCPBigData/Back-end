import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IBrokerageProduct } from 'app/shared/model/brokerage-product.model';
import { BrokerageProductService } from './brokerage-product.service';
import { IBrokerage } from 'app/shared/model/brokerage.model';
import { BrokerageService } from 'app/entities/brokerage';

@Component({
    selector: 'jhi-brokerage-product-update',
    templateUrl: './brokerage-product-update.component.html'
})
export class BrokerageProductUpdateComponent implements OnInit {
    brokerageProduct: IBrokerageProduct;
    isSaving: boolean;

    brokerages: IBrokerage[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private brokerageProductService: BrokerageProductService,
        private brokerageService: BrokerageService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ brokerageProduct }) => {
            this.brokerageProduct = brokerageProduct;
        });
        this.brokerageService.query().subscribe(
            (res: HttpResponse<IBrokerage[]>) => {
                this.brokerages = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.brokerageProduct.id !== undefined) {
            this.subscribeToSaveResponse(this.brokerageProductService.update(this.brokerageProduct));
        } else {
            this.subscribeToSaveResponse(this.brokerageProductService.create(this.brokerageProduct));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBrokerageProduct>>) {
        result.subscribe((res: HttpResponse<IBrokerageProduct>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackBrokerageById(index: number, item: IBrokerage) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
