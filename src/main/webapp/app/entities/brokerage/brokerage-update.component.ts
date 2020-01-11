import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IBrokerage } from 'app/shared/model/brokerage.model';
import { BrokerageService } from './brokerage.service';
import { IBrokerageClient } from 'app/shared/model/brokerage-client.model';
import { BrokerageClientService } from 'app/entities/brokerage-client';
import { IBrokerageProduct } from 'app/shared/model/brokerage-product.model';
import { BrokerageProductService } from 'app/entities/brokerage-product';
import { IBrokerageAssistance } from 'app/shared/model/brokerage-assistance.model';
import { BrokerageAssistanceService } from 'app/entities/brokerage-assistance';

@Component({
    selector: 'jhi-brokerage-update',
    templateUrl: './brokerage-update.component.html'
})
export class BrokerageUpdateComponent implements OnInit {
    brokerage: IBrokerage;
    isSaving: boolean;

    brokerageclients: IBrokerageClient[];

    brokerageproducts: IBrokerageProduct[];

    brokerageassistances: IBrokerageAssistance[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private brokerageService: BrokerageService,
        private brokerageClientService: BrokerageClientService,
        private brokerageProductService: BrokerageProductService,
        private brokerageAssistanceService: BrokerageAssistanceService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ brokerage }) => {
            this.brokerage = brokerage;
        });
        this.brokerageClientService.query().subscribe(
            (res: HttpResponse<IBrokerageClient[]>) => {
                this.brokerageclients = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.brokerageProductService.query().subscribe(
            (res: HttpResponse<IBrokerageProduct[]>) => {
                this.brokerageproducts = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.brokerageAssistanceService.query().subscribe(
            (res: HttpResponse<IBrokerageAssistance[]>) => {
                this.brokerageassistances = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.brokerage.id !== undefined) {
            this.subscribeToSaveResponse(this.brokerageService.update(this.brokerage));
        } else {
            this.subscribeToSaveResponse(this.brokerageService.create(this.brokerage));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBrokerage>>) {
        result.subscribe((res: HttpResponse<IBrokerage>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackBrokerageClientById(index: number, item: IBrokerageClient) {
        return item.id;
    }

    trackBrokerageProductById(index: number, item: IBrokerageProduct) {
        return item.id;
    }

    trackBrokerageAssistanceById(index: number, item: IBrokerageAssistance) {
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
