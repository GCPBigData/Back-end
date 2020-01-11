import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IBrokerageClient } from 'app/shared/model/brokerage-client.model';
import { BrokerageClientService } from './brokerage-client.service';
import { IBrokerage } from 'app/shared/model/brokerage.model';
import { BrokerageService } from 'app/entities/brokerage';

@Component({
    selector: 'jhi-brokerage-client-update',
    templateUrl: './brokerage-client-update.component.html'
})
export class BrokerageClientUpdateComponent implements OnInit {
    brokerageClient: IBrokerageClient;
    isSaving: boolean;

    brokerages: IBrokerage[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private brokerageClientService: BrokerageClientService,
        private brokerageService: BrokerageService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ brokerageClient }) => {
            this.brokerageClient = brokerageClient;
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
        if (this.brokerageClient.id !== undefined) {
            this.subscribeToSaveResponse(this.brokerageClientService.update(this.brokerageClient));
        } else {
            this.subscribeToSaveResponse(this.brokerageClientService.create(this.brokerageClient));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBrokerageClient>>) {
        result.subscribe((res: HttpResponse<IBrokerageClient>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
