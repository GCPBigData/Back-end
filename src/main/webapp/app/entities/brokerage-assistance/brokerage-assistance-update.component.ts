import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IBrokerageAssistance } from 'app/shared/model/brokerage-assistance.model';
import { BrokerageAssistanceService } from './brokerage-assistance.service';
import { IBrokerage } from 'app/shared/model/brokerage.model';
import { BrokerageService } from 'app/entities/brokerage';

@Component({
    selector: 'jhi-brokerage-assistance-update',
    templateUrl: './brokerage-assistance-update.component.html'
})
export class BrokerageAssistanceUpdateComponent implements OnInit {
    brokerageAssistance: IBrokerageAssistance;
    isSaving: boolean;

    brokerages: IBrokerage[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private brokerageAssistanceService: BrokerageAssistanceService,
        private brokerageService: BrokerageService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ brokerageAssistance }) => {
            this.brokerageAssistance = brokerageAssistance;
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
        if (this.brokerageAssistance.id !== undefined) {
            this.subscribeToSaveResponse(this.brokerageAssistanceService.update(this.brokerageAssistance));
        } else {
            this.subscribeToSaveResponse(this.brokerageAssistanceService.create(this.brokerageAssistance));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBrokerageAssistance>>) {
        result.subscribe((res: HttpResponse<IBrokerageAssistance>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
